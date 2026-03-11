#!/bin/bash
# AI Activity Logger - Shared hook script for Claude & Gemini
# Logs only Skill/Agent invocations to .person/logs/activity.log

# Determine project root
PROJECT_DIR="${CLAUDE_PROJECT_DIR:-${GEMINI_PROJECT_DIR:-$(pwd)}}"
LOG_DIR="${PROJECT_DIR}/.person/logs"
LOG_FILE="${LOG_DIR}/activity.log"

# Ensure log directory exists
mkdir -p "${LOG_DIR}"

# Read JSON from stdin
INPUT=$(cat)

# Select processing tool
if command -v jq &>/dev/null; then
  ENTRY=$(echo "${INPUT}" | jq -c '
    {
      tool_name: (.tool_name // null),
      subagent_type: (.tool_input.subagent_type // null),
      description: (.tool_input.description // null),
      skill: (.tool_input.skill // null)
    }
    | to_entries | map(select(.value != null)) | from_entries
    | if length == 0 then empty
      else { timestamp: (now | todate) } + .
      end
  ')
elif command -v python &>/dev/null || command -v python3 &>/dev/null; then
  # Use whichever python is available, avoiding the MS Store shim if possible
  PY_CMD=$(command -v python || command -v python3)
  ENTRY=$(echo "${INPUT}" | "${PY_CMD}" -c "
import sys, json
from datetime import datetime, timezone
input_data = sys.stdin.read()
if input_data.startswith('\ufeff'):
    input_data = input_data.lstrip('\ufeff')
data = json.loads(input_data)
tool_input = data.get('tool_input') or {}
fields = {
    'tool_name': data.get('tool_name'),
    'subagent_type': tool_input.get('subagent_type'),
    'description': tool_input.get('description'),
    'skill': tool_input.get('skill'),
}
fields = {k: v for k, v in fields.items() if v is not None}
if not fields:
    sys.exit(0)
entry = {'timestamp': datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%SZ')}
entry.update(fields)
print(json.dumps(entry, ensure_ascii=False))
")
else
  ENTRY="{\"timestamp\":\"$(date -u +%Y-%m-%dT%H:%M:%SZ)\",\"raw\":$(echo "${INPUT}")}"
fi

# Append to log file only if ENTRY is non-empty
if [ -n "${ENTRY}" ]; then
  echo "${ENTRY}" >> "${LOG_FILE}"
fi

exit 0