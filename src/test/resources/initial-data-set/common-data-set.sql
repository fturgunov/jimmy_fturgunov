DO
$$
    DECLARE
        orgId bigint;
    BEGIN
        truncate table test.reservation cascade;
        truncate table test.car cascade;
    END
$$ LANGUAGE PLPGSQL;