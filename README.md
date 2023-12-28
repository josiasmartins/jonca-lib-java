
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

Example:

```Java
public @interface Logger {

    String value() default "";

}
```
</details>

<details>
  <summary>Processors</summary>
  <h3>Base64ObjectProcessor</h3>

  Example:

```Java
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.lang.reflect.Field;
import java.util.Base64;

public class Base64ObjectProcessor implements Processor {

    private boolean isEncode;

    public Base64ObjectProcessor(Boolean isEncode) {
        this.isEncode = isEncode;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();

        if (body != null) {
            processFields(body);
        }
    }

    private void processFields(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields(); // retorna todos os campos da classe;

        for (Field field : fields) {
            field.setAccessible(true); // deixa os campos acessíveis

            Object value = field.get(obj); //retorna o valor da propriedade

            if (value != null && value instanceof String) {
                // Converte o valor da propriedade para Base64
                String base64Value = this.encodeDecode((String) value);
                field.set(obj, base64Value);
            }


        }

    }

    private String encodeDecode(String value) {
        if (isEncode) {
            return Base64.getEncoder().encodeToString((value).getBytes());
        } else {
            return new String(Base64.getDecoder().decode((String) value));
        }
    }

}
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
