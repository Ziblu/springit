package com.ziblu.springit.domain;

import com.ziblu.springit.service.BeanUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Auditable{
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String body;

    @ManyToOne
    @NonNull
    private Link link;

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
