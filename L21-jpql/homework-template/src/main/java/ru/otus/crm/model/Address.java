package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GeneratorType;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="street")
    private String street;

    public Address(Long id, String street) {
        this.id = null;
        this.street = street;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
