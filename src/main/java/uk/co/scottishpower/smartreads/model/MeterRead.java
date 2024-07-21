package uk.co.scottishpower.smartreads.model;

import jakarta.persistence.*;
import lombok.*;
import uk.co.scottishpower.smartreads.enumerator.MeterReadingType;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "meter_reading")
public class MeterRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "meter_id")
    private long meterId;

    @Column(name = "reading")
    private long reading;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "reading_type")
    private MeterReadingType type;
}
