package ru.yofik.messenger.auth.context.client.dto;

import lombok.*;
import ru.yofik.messenger.auth.context.client.model.ClientPermission;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "client")
public class ClientJpaDto {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean active;

    @ElementCollection(targetClass = ClientPermission.class)
    @CollectionTable(name = "client_permission", joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"))
    @Column(name = "permission")
    @Enumerated(EnumType.STRING)
    private Set<ClientPermission> clientPermissions;
}
