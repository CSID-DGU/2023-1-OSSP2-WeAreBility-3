package com.dongguk.cse.naemansan.util;

import com.dongguk.cse.naemansan.common.ErrorCode;
import com.dongguk.cse.naemansan.common.RestApiException;
import com.dongguk.cse.naemansan.dto.request.UserPaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.io.IOException;

@Slf4j
@Component
public class PaymentUtil {
    private final IamportClient iamportClient = new IamportClient("1656610451202616",
            "Go0Kmco42ux0HdqGua61tWdapOpnYMdvPxd0nJJQngm6DHsICmfIkUC2b615M8z0JTXFtOKaK7DNahLj");

    public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    public Boolean verifyIamport(IamportResponse<Payment> irsp, Long amount) {
        if (irsp.getResponse().getAmount().intValue() != amount) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
