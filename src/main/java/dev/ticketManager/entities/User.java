package dev.ticketManager.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("user_data")
public class User {
    @Id
    private Long id;

    @NotBlank(message = "не должен быть пустым")
    @Size(min = 3, max = 25, message = "должен содержать от 3 до 25 символов")
    private String login;

    @NotBlank(message = "не должен быть пустым")
    @Size(min = 6, max = 25, message = "должен содержать от 6 до 25 символов")
    private String password;

    @NotBlank(message = "не должен быть пустым")
    @Size(max = 100, message = "должен содержать не более 100 символов")
    private String fio;
}
