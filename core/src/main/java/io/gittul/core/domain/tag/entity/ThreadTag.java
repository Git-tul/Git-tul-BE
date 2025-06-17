package io.gittul.core.domain.tag.entity;

import io.gittul.core.domain.thread.entity.Thread;
import io.gittul.core.global.jpa.EntityTimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "THREAD_TAG")
public class ThreadTag extends EntityTimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THREAD_TAG_ID", nullable = false)
    private Long threadTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THREAD_ID", nullable = false)
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAG_ID", nullable = false)
    private Tag tag;

    public ThreadTag(Thread thread, Tag tag) {
        this.thread = thread;
        this.tag = tag;
    }
}
