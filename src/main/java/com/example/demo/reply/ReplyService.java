package com.example.demo.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    // TODO: 구현 필요
}
