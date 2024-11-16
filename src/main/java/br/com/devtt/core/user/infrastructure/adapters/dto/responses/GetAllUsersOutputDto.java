package br.com.devtt.core.user.infrastructure.adapters.dto.responses;

import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.AbstractPagedOutputDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@SuperBuilder
@Getter
@EqualsAndHashCode(of = "users", callSuper = false)
public class GetAllUsersOutputDto extends AbstractPagedOutputDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -5759546880803564199L;
    private final List<GetUserOutputDto> users;
}