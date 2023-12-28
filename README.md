
# jonca-library-java

## Features
...
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
</details>


## Documentation

For comprehensive documentation and usage examples, please visit the project's GitHub repository: https://github.com/josiasmartins/jonca-library-node

## Contributing

Contributions are always welcome! Fork the repository, make your changes, and submit a pull request.

## License

This project is licensed under the MIT License.
