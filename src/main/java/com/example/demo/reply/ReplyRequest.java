package com.example.demo.reply;

import lombok.Data;

public class ReplyRequest {

    @Data
    public static class Save {
        private String comment;
        private Integer boardId;
    }
}
