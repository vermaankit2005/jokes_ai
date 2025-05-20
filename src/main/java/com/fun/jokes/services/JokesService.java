package com.fun.jokes.services;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class JokesService {

    private final OllamaChatModel ollamaChatClient;

    public JokesService(OllamaChatModel ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    public Flux<ChatResponse> getFunnyJoke(String topic) {
        String promptText = String.format(
                "Tell me a funny joke with theme of %s. Make it original and witty. Provide the joke directly without any pretext! Don't repeat the joke", topic
        );
        Prompt prompt = new Prompt(promptText);
        //return ollamaChatClient.call(prompt).getResult().getOutput().getText();
        return ollamaChatClient.stream(prompt);
    }

}