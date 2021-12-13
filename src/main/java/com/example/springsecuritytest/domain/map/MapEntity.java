package com.example.springsecuritytest.domain.map;

import com.example.springsecuritytest.domain.bbs.BbsEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "JW_MAP")
public class MapEntity {

    @Id
    @SequenceGenerator(
            name = "MAP_SEQ_GEN",
            sequenceName = "JW_MAP_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_SEQ_GEN")
    private Long id;

    @Column
    private double latitude = 0.0;

    @Column
    private double longitude = 0.0;

    @Column
    private String placeName;

    @OneToMany(mappedBy = "map", cascade = CascadeType.PERSIST)
    private List<BbsEntity> bbsEntityList = new ArrayList<>();

    public void addBbs(BbsEntity bbsEntity) {
        this.getBbsEntityList().add(bbsEntity);
        bbsEntity.setMap(this);
    }

    @Builder
    public MapEntity(Long id, double latitude, double longitude, String placeName) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }
}
