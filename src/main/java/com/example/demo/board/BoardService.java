package com.example.demo.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * DTO는 Service에서 만든다. Entity를 Controller에 전달하지 않는다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.ListDTO> 게시글목록보기(int page) {
        int size = 3; // 한페이지에 보여줄 개수 (limit = size)
        int limit = size;
        int offset = page * size; // 시작 인덱스

        List<Board> boards = boardRepository.findAll(limit, offset);
        return boards.stream().map(BoardResponse.ListDTO::new).toList();
    }
}
