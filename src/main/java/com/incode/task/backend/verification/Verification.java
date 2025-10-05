package com.incode.task.backend.verification;

import com.incode.task.backend.backend.ThirdPartyResultSource;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "verification")
public class Verification {

    @Id
    private UUID verificationId;

    @Column
    private String queryText;

    @Column
    private LocalDateTime timestamp;

    @Column
    @Enumerated(EnumType.STRING)
    private ThirdPartyResultSource source;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private BackendQueryResult result;

    public Verification() {

    }

    public Verification(UUID verificationId, String queryText, LocalDateTime timestamp, ThirdPartyResultSource source,
                        BackendQueryResult result) {

        this.verificationId = verificationId;
        this.queryText = queryText;
        this.timestamp = timestamp;
        this.source = source;
        this.result = result;
    }

    public UUID getVerificationId() {

        return verificationId;
    }

    public void setVerificationId(UUID verificationId) {

        this.verificationId = verificationId;
    }

    public String getQueryText() {

        return queryText;
    }

    public void setQueryText(String queryText) {

        this.queryText = queryText;
    }

    public LocalDateTime getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {

        this.timestamp = timestamp;
    }

    public ThirdPartyResultSource getSource() {

        return source;
    }

    public void setSource(ThirdPartyResultSource source) {

        this.source = source;
    }

    public BackendQueryResult getResult() {

        return result;
    }

    public void setResult(BackendQueryResult result) {

        this.result = result;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;
        Verification that = (Verification) o;
        return Objects.equals(getVerificationId(), that.getVerificationId()) && Objects.equals(getQueryText(), that.getQueryText()) && Objects.equals(getTimestamp(), that.getTimestamp()) && getSource() == that.getSource() && Objects.equals(getResult(), that.getResult());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getVerificationId(), getQueryText(), getTimestamp(), getSource(), getResult());
    }
}
