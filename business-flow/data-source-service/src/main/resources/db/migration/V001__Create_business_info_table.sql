-- Check if the table exists before creating it
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'business_info') THEN
CREATE TABLE public.business_info (
                                      id BIGSERIAL PRIMARY KEY,  -- Change SERIAL to BIGSERIAL
                                      name VARCHAR(255) NOT NULL,
                                      description TEXT
);

-- Add a comment to the table
COMMENT ON TABLE public.business_info IS 'Stores basic information about businesses';

        -- Add comments to the columns
        COMMENT ON COLUMN public.business_info.id IS 'Auto-generated unique identifier for each business';
        COMMENT ON COLUMN public.business_info.name IS 'Name of the business';
        COMMENT ON COLUMN public.business_info.description IS 'Description of the business';
END IF;
END $$;
