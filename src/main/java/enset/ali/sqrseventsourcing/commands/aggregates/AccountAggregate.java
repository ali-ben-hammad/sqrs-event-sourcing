package enset.ali.sqrseventsourcing.commands.aggregates;

import enset.ali.sqrseventsourcing.commonApi.commands.CreateAccountCommand;
import enset.ali.sqrseventsourcing.commonApi.enums.AccountStatus;
import enset.ali.sqrseventsourcing.commonApi.events.AccountActivatedEvent;
import enset.ali.sqrseventsourcing.commonApi.events.AccountCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
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

        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getAccountBalance(),
                createAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent){
        this.AccountId = accountCreatedEvent.getId();
        this.accountBalance = accountCreatedEvent.getInitialBalance();
        this.currency = accountCreatedEvent.getCurrency();
        this.status = AccountStatus.CREATED;

        AggregateLifecycle.apply(new AccountActivatedEvent(AccountId, AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent accountActivatedEvent){
        this.status = accountActivatedEvent.getStatus();
    }
}
