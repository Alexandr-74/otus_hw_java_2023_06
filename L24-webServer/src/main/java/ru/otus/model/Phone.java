package ru.otus.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "phone")
@ToString
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name="number")
    @Expose
    private String number;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Client client;


    public Phone(Long id, String number) {
        this.id=null;
        this.number=number;
    }


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
