package ru.otus.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
@ToString
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    @Expose
    private Long id;

    @Column(name = "name")
    @Expose
    private String name;

    @Column(name = "login")
    @Expose
    private String login;

    @Column(name = "password")
    @Expose
    private String password;

    @OneToOne(optional=false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Expose
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade = CascadeType.ALL)
    @Expose
    private List<Phone> phones;

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.phones = phones.stream().map(phone -> new Phone(phone.getId(), phone.getNumber(), this)).toList();
    }

    public Client(Long id, String name, Address address, List<Phone> phones, String login, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.phones.forEach(ph->{
            if (ph.getClient() == null) {
                ph.setClient(this);
            }
        });
        this.login = login;
        this.password = password;
    }


    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phones, this.login, this.password);
    }
}
