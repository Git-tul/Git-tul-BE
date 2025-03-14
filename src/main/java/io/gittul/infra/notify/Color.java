package io.gittul.infra.notify;

import lombok.Getter;

@Getter
public enum Color {
    GREEN("#36a64f"),
    RED("#ff0000"),
    BLUE("#0000ff"),
    YELLOW("#ffff00"),
    BLACK("#000000"),
    WHITE("#ffffff");

    private final String code;

    Color(String color) {
        this.code = color;
    }

}
