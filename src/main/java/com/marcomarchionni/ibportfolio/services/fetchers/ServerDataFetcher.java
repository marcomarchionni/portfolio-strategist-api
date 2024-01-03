package com.marcomarchionni.ibportfolio.services.fetchers;

import com.marcomarchionni.ibportfolio.dtos.flex.FlexQueryResponseDto;
import com.marcomarchionni.ibportfolio.dtos.flex.FlexStatementResponseDto;
import com.marcomarchionni.ibportfolio.dtos.request.UpdateContextDto;
import com.marcomarchionni.ibportfolio.errorhandling.exceptions.IbServerErrorException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ServerDataFetcher implements DataFetcher {

    private final RestTemplate restTemplate;
    private final String authUrl;
    private final String reqPath;
    private final int maxAttempts;
    private final long retryDelay;

    public ServerDataFetcher(RestTemplate restTemplate,
                             @Value("${ib.auth-url}") String authUrl,
                             @Value("${ib.req-path}") String reqPath,
                             @Value("${ib.max-attempts}") int maxAttempts,
                             @Value("${ib.retry-delay}") long retryDelay) {
        this.restTemplate = restTemplate;
        this.authUrl = authUrl;
        this.reqPath = reqPath;
        this.maxAttempts = maxAttempts;
        this.retryDelay = retryDelay;
    }

    @Override
    public FlexQueryResponseDto fetch(UpdateContextDto context) {
        // Extract token and query id from context
        String token = context.getToken();
        String queryId = context.getQueryId();

        // Create request entity with headers
        HttpEntity<String> requestEntity = createRequestWithHeaders();

        // Fetch statement response dto from server
        FlexStatementResponseDto statementResponseDto = executeRequestWithRetry(authUrl, requestEntity,
                FlexStatementResponseDto.class, token, queryId);

        // Extract url and reference code from statement response dto
        String downloadUrl = statementResponseDto.getUrl() + reqPath;
        String referenceCode = statementResponseDto.getReferenceCode();

        // Fetch flex query response dto using url and code from first response

        return executeRequestWithRetry(downloadUrl, requestEntity, FlexQueryResponseDto.class, token, referenceCode);
    }

    private HttpEntity<String> createRequestWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Technology/Version");
        return new HttpEntity<>(headers);
    }

    private <T> @NotNull T executeRequestWithRetry(String url, HttpEntity<?> requestEntity,
                                                   Class<T> responseType, Object... uriVariables) {
        int attempts = 1;
        long delay = retryDelay;
        ResponseEntity<T> response = null;
        while (attempts <= maxAttempts) {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    responseType,
                    uriVariables);

            // If response is OK and dto is valid, return dto
            log.info("Executing request to IB server... Attempt: {}", attempts);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Valid response from IB server, returning... Response: {}", response);
                return response.getBody();
            }

            // If response is not OK, wait and retry
            log.info("Invalid response from IB server, retrying... Response: {}", response);
            attempts++;
            try {
                Thread.sleep(delay);
                delay *= 2;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IbServerErrorException("Interrupted while waiting to retry");
            }
        }
        if (response != null) throw new IbServerErrorException(response);
        throw new IbServerErrorException("Unable to fetch data from IB server");
    }
}

