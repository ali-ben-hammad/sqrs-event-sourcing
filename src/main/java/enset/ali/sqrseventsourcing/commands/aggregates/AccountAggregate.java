package enset.ali.sqrseventsourcing.commands.aggregates;

import enset.ali.sqrseventsourcing.commonApi.commands.CreateAccountCommand;
import enset.ali.sqrseventsourcing.commonApi.enums.AccountStatus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;

public class AccountAggregate {
    @AggregateIdentifier
    private String AccountId;
    private double accountBalance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // Required by Axon Framework
    }

    @CommandHandler // listens on the command bus for CreateAccountCommand
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getAccountBalance() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be less than 0");
        }
    }
}
