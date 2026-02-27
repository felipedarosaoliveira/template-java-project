package br.com.company.core.domain.model;

import java.util.Objects;

/**
 * Domain Entity - Pure Java (no framework dependencies)
 * Represents a core business concept
 */
public class Message {

    private final String id;
    private final String content;

    public Message(String id, String content) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

