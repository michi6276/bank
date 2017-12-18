package de.oth.gmeiner.swgmeiner.util;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class LongIdEntity extends SingleIdEntity<Long> {
    
    @Id
    protected Long id;
    
    protected LongIdEntity(){
    }
    
    protected LongIdEntity(Long id){
        this.id = id;
    }

    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id){
        this.id=id;
    }

}
