package com.fun.jokes.views;

import com.fun.jokes.services.JokesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import reactor.core.Disposable;

/**
 * The main view contains a button and a click listener.
 */
@PageTitle("Hello World")
@Route("")
public class HelloWorldView extends VerticalLayout {

    private Button sayHello;
    private Disposable jokeSubscription;

    public HelloWorldView(JokesService service) {
        ComboBox<String> comboBox = new ComboBox<>("Choose an option");
        comboBox.setItems("Dad Jokes", "Knock-Knock Jokes", "Animal Jokes");
        comboBox.setValue("Dad Jokes");

        TextArea jokeText = new TextArea();
        jokeText.setWidthFull();
        jokeText.setHeight("300px");

        sayHello = new Button("Generate");
        sayHello.addClickListener(e -> {
            //service.getFunnyJoke(comboBox.getValue()).toStream(10);
            if (jokeSubscription != null && !jokeSubscription.isDisposed()) {
                jokeSubscription.dispose();
            }

            jokeText.setValue("");

            jokeSubscription = service.getFunnyJoke(comboBox.getValue()).subscribe(chatResponse -> {
                String token = chatResponse.getResult().getOutput().getText(); // Adjust as needed for your ChatResponse
                jokeText.getUI().ifPresent(ui ->
                        ui.access(() -> {
                            jokeText.setValue(jokeText.getValue() + token);
                        })
                );
            });
        });

        setMargin(true);
        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        add(comboBox, sayHello, jokeText);

        addDetachListener(event -> {
            if (jokeSubscription != null && !jokeSubscription.isDisposed()) {
                jokeSubscription.dispose();
            }
        });
    }
}
