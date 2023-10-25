package softuni.WebFinderserver.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessDto {

    private boolean isCreated;

    public SuccessDto setCreated(boolean created) {
        isCreated = created;
        return this;
    }

}
