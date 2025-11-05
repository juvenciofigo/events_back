package com.providences.events.config;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fc.sdk.APIContext;
import com.fc.sdk.APIMethodType;
import com.fc.sdk.APIRequest;
import com.fc.sdk.APIResponse;

@Service
public class MpesaService {
    private final String apiKey = "y2rq82uyexr8lr93lt8y157mc50b09dy";
    private final String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmptSWqV7cGUUJJhUBxsMLonux24u+FoTlrb+4Kgc6092JIszmI1QUoMohaDDXSVueXx6IXwYGsjjWY32HGXj1iQhkALXfObJ4DqXn5h6E8y5/xQYNAyd5bpN5Z8r892B6toGzZQVB7qtebH4apDjmvTi5FGZVjVYxalyyQkj4uQbbRQjgCkubSi45Xl4CGtLqZztsKssWz3mcKncgTnq3DHGYYEYiKq0xIj100LGbnvNz20Sgqmw/cH+Bua4GJsWYLEqf/h/yiMgiBbxFxsnwZl0im5vXDlwKPw+QnO2fscDhxZFAwV06bgG0oEoWm9FnjMsfvwm0rUNYFlZ+TOtCEhmhtFp+Tsx9jPCuOd5h2emGdSKD8A6jtwhNa7oQ8RtLEEqwAn44orENa1ibOkxMiiiFpmmJkwgZPOG/zMCjXIrrhDWTDUOZaPx/lEQoInJoE2i43VN/HTGCCw8dKQAwg0jsEXau5ixD0GUothqvuX3B9taoeoFAIvUPEq35YulprMM7ThdKodSHvhnwKG82dCsodRwY428kg2xM/UjiTENog4B6zzZfPhMxFlOSFX4MnrqkAS+8Jamhy1GgoHkEMrsT5+/ofjCx0HjKbT5NuA2V/lmzgJLl3jIERadLzuTYnKGWxVJcGLkWXlEPYLbiaKzbJb2sYxt+Kt5OxQqC1MCAwEAAQ==";

    public APIResponse executePayment(String transactionRef, String thirdPartyRef, String payerNum, BigDecimal amount) {
        try {
            APIContext context = new APIContext();
            context.setApiKey(apiKey);
            context.setPublicKey(publicKey);
            context.setSsl(true);
            context.setMethodType(APIMethodType.POST);
            context.setAddress("api.sandbox.vm.co.mz");
            context.setPort(18352);
            context.setPath("/ipg/v1x/c2bPayment/singleStage/");
            context.addHeader("Origin", "*");

            // Parâmetros
            context.addParameter("input_TransactionReference", transactionRef);
            context.addParameter("input_ThirdPartyReference", thirdPartyRef);
            context.addParameter("input_CustomerMSISDN", "258" + payerNum);
            context.addParameter("input_Amount", amount.toString());
            context.addParameter("input_ServiceProviderCode", "171717");

            APIRequest request = new APIRequest(context);
            APIResponse response = request.execute();

            if (response != null) {
                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Reason: " + response.getReason());
                System.out.println("Result: " + response.getResult());

                for (Map.Entry<String, String> entry : response.getParameters().entrySet()) {
                    System.out.println(entry.getKey() + ": " + response.getParameter(entry.getKey()));
                }
            } else {
                System.out.println("Nenhuma resposta do servidor M-Pesa.");
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
