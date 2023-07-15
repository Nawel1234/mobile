package com.esprit.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.list.MultiList;
import java.util.HashMap;
import java.util.Map;

public class ChatBotForm extends Form {
    private ListModel<String> messageListModel;
    private MultiList messageList;
    private TextField userInputField;
    private Button sendButton;
    private Button backButton;

    private Map<String, String> responses;

    public ChatBotForm(Form previousForm) {
        super("Chatbot", BoxLayout.y());

        messageListModel = new DefaultListModel<>();
        messageList = new MultiList(messageListModel);
        userInputField = new TextField();
        sendButton = new Button("Envoyer");
        backButton = new Button("Retour");

        Container messageListContainer = new Container();
        messageListContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        messageListContainer.add(messageList);

        Container inputContainer = new Container(new FlowLayout(Component.CENTER));
        inputContainer.add(userInputField);
        inputContainer.add(sendButton);

        Container buttonContainer = new Container(new FlowLayout(Component.CENTER));
        buttonContainer.add(backButton);

        add(messageListContainer);
        add(inputContainer);
        add(buttonContainer);

        responses = createChatbotResponses();

        sendButton.addActionListener(evt -> sendMessage());
        backButton.addActionListener(evt -> previousForm.showBack());

        userInputField.addActionListener(evt -> {
            String userInput = userInputField.getText();

            if (userInput != null && !userInput.isEmpty()) {
                addMessage("Vous: " + userInput);

                // Générer la réponse du chatbot
                String response = generateChatbotResponse(userInput);
                addMessage("Chatbot: " + response);

                userInputField.clear();
            }
        });
    }

    public void showForm() {
        this.show();
    }

    private void sendMessage() {
        String userInput = userInputField.getText();

        if (userInput != null && !userInput.isEmpty()) {
            addMessage("Vous: " + userInput);

            // Générer la réponse du chatbot
            String response = generateChatbotResponse(userInput);
            addMessage("Chatbot: " + response);

            userInputField.clear();
        }
    }

    private void addMessage(String message) {
        ((DefaultListModel<String>) messageListModel).addItem(message);
    }

    private String generateChatbotResponse(String userInput) {
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            String keyword = entry.getKey();
            String response = entry.getValue();

            if (userInput.toLowerCase().contains(keyword.toLowerCase())) {
                return response;
            }
        }

        return "Désolé, je n'ai pas compris. Pouvez-vous reformuler votre question ?";
    }

    private Map<String, String> createChatbotResponses() {
        Map<String, String> responses = new HashMap<>();
        responses.put("bonjour", "Bonjour ! Comment puis-je vous aider ?");
        responses.put("tarif", "Les tarifs des événements peuvent varier en fonction de chaque événement.");
        responses.put("coach", "Les coachs sont disponibles dans notre salle.");
        responses.put("modes de paiement", "Nous acceptons les paiements par carte de crédit, virement bancaire et espèces.");
        responses.put("merci", "De rien ! Je suis là pour vous aider.");
        responses.put("aurevoir", "Au revoir ! N'hésitez pas à revenir si vous avez d'autres questions.");

        return responses;
    }
}
