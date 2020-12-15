package io.tatum.model.request;

import io.tatum.model.response.ledger.Fiat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class CustomerUpdate {

    @Size(min = 2, max = 2)
    private Country customerCountry;

    private Fiat accountingCurrency;

    private Country providerCountry;

    @Size(min = 1, max = 100)
    private String externalId;

}
