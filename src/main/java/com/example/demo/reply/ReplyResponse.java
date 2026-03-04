package com.example.demo.reply;

import lombok.Data;

import java.time.LocalDateTime;

public class ReplyResponse {

    @Data
    public static class Detail {
        private Integer id;
        private String comment;
        private String username;
        private LocalDateTime createdAt;

        public Detail(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.username = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt();
        }
    }
}
