package ru.otus.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @Expose
    private Long id;

    @Column(name="street")
    @Expose
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
