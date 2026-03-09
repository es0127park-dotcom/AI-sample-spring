# Workflow: 주소 연동 API (카카오/다음 우편번호 서비스)

본 문서는 회원가입 시 정확한 주소 데이터를 수집하기 위해 카카오 우편번호 API를 연동하는 핵심 실행 흐름을 정의합니다.

---

## 1. 기능 개요 (Overview)

- **목적**: 사용자가 직접 주소를 타이핑하여 발생하는 오타를 방지하고, 규격화된 주소 데이터를 수집함.
- **핵심 기술**: 카카오(다음) 우편번호 서비스 API (팝업 방식).
- **데이터 정책**:
  - **우편번호/도로명주소**: 검색 API를 통해서만 입력 가능 (`readonly`).
  - **상세주소**: 사용자가 직접 입력 (`동/호수`).

---

## 2. 실행 흐름 (Execution Flow)

### Step 1: 외부 라이브러리 로드

HTML 템플릿 하단에 카카오 API 스크립트를 포함합니다.

```html
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
```

### Step 2: HTML 폼 구조 설계

사용자가 입력을 제어할 수 없도록 `readonly` 속성을 적절히 배치합니다.

- `postcode`: 우편번호 필드 (readonly)
- `address`: 기본 주소 필드 (readonly)
- `detailAddress`: 상세 주소 필드 (입력 가능)
- `extraAddress`: 참고 항목 필드 (readonly)

### Step 3: API 호출 및 데이터 매핑 (JS)

1. `new daum.Postcode({...})` 객체를 생성합니다.
2. `oncomplete` 콜백 함수 내에서 `data` 객체의 값을 각 input 필드에 할당합니다.
3. 데이터 할당 직후 `detailAddress.focus()`를 호출하여 사용자 흐름을 이어갑니다.

---

## 3. 핵심 코드 (Core Logic)

### JavaScript 핵심 로직

```javascript
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 1. 도로명 주소 변수 추출
      let addr = data.roadAddress;

      // 2. 참고항목(건물명 등) 조립 로직
      let extraAddr = "";
      if (data.bname !== "" && /[동|로|가]$/g.test(data.bname))
        extraAddr += data.bname;
      if (data.buildingName !== "" && data.apartment === "Y") {
        extraAddr +=
          extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
      }
      if (extraAddr !== "") extraAddr = " (" + extraAddr + ")";

      // 3. DOM 필드에 데이터 매핑
      document.querySelector("#postcode").value = data.zonecode;
      document.querySelector("#address").value = addr;
      document.querySelector("#extraAddress").value = extraAddr;

      // 4. 상세주소 필드로 포커스 이동
      document.querySelector("#detailAddress").focus();
    },
  }).open();
}
```

---

## 4. 검증 체크리스트 (Definition of Done)

- [ ] 주소 검색 버튼 클릭 시 팝업창이 뜨는가?
- [ ] 주소 선택 시 우편번호, 주소, 참고항목 필드가 자동 입력되는가?
- [ ] 입력된 주소와 우편번호 필드는 사용자가 직접 수정할 수 없는가 (`readonly`)?
- [ ] 주소 선택 직후 포커스가 '상세주소' 필드로 이동하는가?
