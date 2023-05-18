package org.whitebox.howlook.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class WeatherDTO {
    @NotNull
    Long temperature; // 섭씨 온도

    @NotNull
    Long weather; // 날씨 : 1 맑음 3 구름 4 흐림

    public WeatherDTO(Long t, Long w)
    {
        this.temperature = t;
        this.weather = w;
    }
}
