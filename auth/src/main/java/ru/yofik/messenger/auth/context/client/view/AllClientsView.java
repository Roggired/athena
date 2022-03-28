package ru.yofik.messenger.auth.context.client.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllClientsView {
    public final List<ClientView> clientViews;
}
