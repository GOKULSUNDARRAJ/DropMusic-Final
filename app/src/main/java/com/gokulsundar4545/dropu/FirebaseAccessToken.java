package com.gokulsundar4545.dropu;



import com.google.api.client.util.Lists;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FirebaseAccessToken {

    private static final String FIREBASE_MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "    \"type\": \"service_account\",\n" +
                    "    \"project_id\": \"drop-track-e203a\",\n" +
                    "    \"private_key_id\": \"b27a2a8760ffea924354441bdd1f9f0a000b47ce\",\n" +
                    "    \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCu8MaEvHiFwmXd\\nXRe/jDpgygIqbthQjCgoQxf3P787djkkZNvlZy15eiiWD3rPL2YoRsDWVeDtxZrp\\nr0I2mxH9dA7WJXoZvs+oCUDaGYBeiFb/TU/yGBGW/9okg0FBGjCXa0b7PKEVj/Od\\nYwxC+qdskbd1NWJB2hxsfBGQALVPGyrAbk2FAZ83uskKFXtL014nppM+DJkILi3u\\nQXG3A5RCqpA+4VTTVIk/ySDctjzSKo5SHU90SSEJwU6JK1lBsP+00aLV+CmvZa7W\\nMgsrXXyVxP6IT7HRr3x3L6iXGHfEIwhShFhXRSDeopzZ/T6BhREW9fAKPtQQSc8y\\nHyiFPqS9AgMBAAECggEAA9XN+Fd8aexI2OlDTcT4e6o6EW2UKvHjqHxOEHLSka5m\\nEszuQDF6nvzlWmLU/alLm0rvy9Mepv/LxV88+577LjwG3j18l4dmHIg1ZoKkHI5v\\nH9I1VDigXBfYU7AX9GIrWgp1SCk8W9Fn1RW4jVha5sesBrMIqCpssJkotmg5kVYd\\nBgBv7ArRgbh5ihV5HtWFBRRA9rSEUf8T07rDRo+XJJhWXG7T6r1QAwlRyNt/ZeH4\\n040WaN+xdhSwWDISoxqFpZ8T0GDIe0OY17HA6vuAjQsnKF8a40UxnhlZ3U5HzySy\\n4G0Jmop79Q8aaQa/XjloUkiL6T3A4c5fWKcu884gEQKBgQDxch8UA2wqCJZaEpKn\\n8ruvCvg0zxvnHaQ7LnSbEJl4sBk/SfEGpt/8XBOja7GGLLhBdX5tHT+3ePXDttWZ\\nUemucorU/T/jBGuR2SNrlGKQHZbDBczBSzvYon6Xzz71ROvuXW1FUOjtkqN2UBJg\\nJzhVp3o4uLKuDOkjPcCLTx4sEQKBgQC5fGBBIIWis8GKtTnTEhAXxnAWXe1wTI5Y\\nWJ4y7yLBPy7VyUumOiJdV6UhHN4AvvMK4vYcNfdP298FkEob2JY6j1gN9GjVNBKM\\np8fpF2rnYhgw/iqEH/vVfagHiCtGG4Ws2DIbm6jGBTQ1hv5xT8hWGy7iyafsLuWz\\nTMk8as9J7QKBgQDUlZ2lemn3DH6NKgM0mrUDpw3pOV2g/WlHrlx+13u5Vpu0LL9q\\nEu3t/YhRABZbQ+6ru+6n1fPu8DK7srSY/RVaQoHYjG5+zLqFvQDBzPGHYHYImoew\\nvTQhRtldgt01CQ0OX/ZA7L9Zxh3koxW60W15hS9I196C/t0a6Vkpj2zKcQKBgBGZ\\nNWC4fzVirLhGXYgjLJe04imdG8wN12uWbNwUSE3hesYZ5S2OUnhcunQb0wxrEZA0\\n3Hjsvdw71jsm54Eg2ZAQzZ/1UoT3/dKOkK8S47e0XOs5Ejua9T/aPjkIuGJcdiBi\\nGs73w9mV4NA7UKTblvxkc9xcmawU/wI2BNnQoH45AoGBANvlW4K0qX/GHtWCfsU4\\n0VEuP2/Cz+MiX0vOkqcqXmNf7aJnqmX/va5gnLTyb1ozcvo7vDuUG+NNGPt6HNVK\\n2krw1cQrckbrvi21QXBRZuC75hoaS0ff/wmzQQtf+r3l+VEsVWiDa8MSDXbzhA/x\\n5bfMUkYZhSB/gIiOsQivzsXq\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "    \"client_email\": \"firebase-adminsdk-k22ax@drop-track-e203a.iam.gserviceaccount.com\",\n" +
                    "    \"client_id\": \"112786146032785690997\",\n" +
                    "    \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "    \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "    \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "    \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-k22ax%40drop-track-e203a.iam.gserviceaccount.com\",\n" +
                    "    \"universe_domain\": \"googleapis.com\"\n" +
                    "}";

            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            List<String> scopes = Arrays.asList(FIREBASE_MESSAGING_SCOPE);

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(scopes);

            googleCredentials.refresh();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            return null;
        }
    }
}


