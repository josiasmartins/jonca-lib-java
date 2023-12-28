
# jonca-library-java

## Features
Biblioteca com vários peças estruturais e não, para ajudar em desenvolvimento

## Installation

Bash
```bash
mvn install jonca-library-java
```
## Usage

<details>
  <summary>Annotations</summary>
  <h3>Logger</h3>
  <h5>Anotacao para logar os dados no logstash</h5>

Example:

```Java
import com.techbuzzblogs.rest.camelproject.decorators.Logger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDetailsType {

    @Logger
    private String carName;

    @Logger
    private String carModel;
    private String company;

}

```
</details>

<details>
  <summary>Processors</summary>
  <h3>Base64ObjectProcessor</h3>
  <h5>Transforma todas as propriedades da classe java em base 64 ENCODE | DECODE</h5>

  Example:

```Java
from("direct:start")
    .process(new Base64ObjectProcessor(false))  // Decode String fields
    .to("mock:result");

// agora com encode
from("direct:start")
    .process(new Base64ObjectProcessor(true))  // Encode String fields
    .to("mock:result");
```
</details>

<details>
  <summary>Routes</summary>
</details>

<details>
  <summary>Utils</summary>

  <h3>LoggerUtil</h3>
  <h5>
    Percorre todas as propriedades de qualquer objeto (simples, complexo), e salva no Map em todas as propriedades salvas.<br>
    Obs: essa verificacao faz com base no annotation Logger
  </h5>

  Example:

  ```java
   @Override
    public void process(Exchange exchange) throws Exception {
        CarDetailsType body = exchange.getIn().getBody(CarDetailsType.class);

        Map<String, String> mapper = LoggerMethodUtil.extractProperties(body);
    }
  ```
</details>


## Documentation

For comprehensive documentation and usage examples, please visit the project's GitHub repository: https://github.com/josiasmartins/jonca-library-node

## Contributing

Contributions are always welcome! Fork the repository, make your changes, and submit a pull request.

## License

This project is licensed under the MIT License.
