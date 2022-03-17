package ru.yofik.messenger.controller.v1.helper;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.util.stream.Collectors;

public final class ErrorMessageHelper {
    private ErrorMessageHelper() {
    }

    public static String buildErrorMessageForMismatchedInputException(MismatchedInputException exception) {
        final var pathItemStrings = exception.getPath().stream()
                .map(reference -> {
                    final String pathItemString;
                    if (reference.getFieldName() != null) {
                        pathItemString = "." + reference.getFieldName();
                    } else if (reference.getIndex() > -1) {
                        pathItemString = "[" + reference.getIndex() + "]";
                    } else {
                        pathItemString = "";
                    }
                    return pathItemString;
                })
                .collect(Collectors.toList());

        var pathString = String.join("", pathItemStrings);
        if (pathString.startsWith(".")) {
            pathString = pathString.substring(1);
        }

        return "Invalid value for " + pathString;
    }
}

