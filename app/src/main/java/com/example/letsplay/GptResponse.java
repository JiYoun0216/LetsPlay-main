package com.example.letsplay;

import java.util.List;

//public class GptResponse {
//    private List<Choice> choices;
//
//    public static class Choice {
//        private Message message;
//
//        public Message getMessage() {
//            return message;
//        }
//    }
//
//    public static class Message {
//        private String role;
//        private String content;
//
//        public String getContent() {
//            return content;
//        }
//    }
//
//    public String getGeneratedText() {
//        return choices != null && !choices.isEmpty()
//                ? choices.get(0).getMessage().getContent()
//                : null;
//    }
//}

public class GptResponse {

    private String reply;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}

