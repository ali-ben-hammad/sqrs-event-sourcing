package enset.ali.sqrseventsourcing.commands.controllers;

import enset.ali.sqrseventsourcing.commonApi.commands.CreateAccountCommand;
import enset.ali.sqrseventsourcing.commonApi.dtos.CreateAccountRequestDTO;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/account")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    @PostMapping(path = "/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO createAccountDTO) {
        CompletableFuture<String> commandResponse = commandGateway.send(
                new CreateAccountCommand(
                        UUID.randomUUID().toString(),
                        createAccountDTO.getAccountBalance(),
                        createAccountDTO.getCurrency()
                ));
        return commandResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        ResponseEntity<String> entity = new ResponseEntity<String>(
                exception.getMessage(),
                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
        );

        return entity;
    }
}
