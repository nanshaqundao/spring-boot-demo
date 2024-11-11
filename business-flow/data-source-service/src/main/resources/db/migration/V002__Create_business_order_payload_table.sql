-- Check if the table exists before creating it
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'business_order_payload') THEN
CREATE TABLE public.business_order_payload (
                                               id BIGSERIAL PRIMARY KEY,  -- Auto-generated unique identifier
                                               name VARCHAR(255) NOT NULL,  -- Name of the order
                                               order_number VARCHAR(255) NOT NULL,  -- Order number for identification
                                               content TEXT  -- Content or details of the order
);

-- Add a comment to the table
COMMENT ON TABLE public.business_order_payload IS 'Stores order payload information related to businesses';

        -- Add comments to the columns
        COMMENT ON COLUMN public.business_order_payload.id IS 'Auto-generated unique identifier for each order';
        COMMENT ON COLUMN public.business_order_payload.name IS 'Name associated with the order';
        COMMENT ON COLUMN public.business_order_payload.order_number IS 'Unique order number';
        COMMENT ON COLUMN public.business_order_payload.content IS 'Details or content of the order';
END IF;
END $$;
