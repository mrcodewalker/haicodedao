package com.example.codewalker.kma.dtos;

import com.example.codewalker.kma.models.Score;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class ScoreUpdateContainerDTO {
    private Score score;
    private boolean isUpdate;
}
