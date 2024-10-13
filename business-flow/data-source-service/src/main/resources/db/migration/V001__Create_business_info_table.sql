-- Create business_info table
CREATE TABLE public.business_info (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      description TEXT
);

-- Add a comment to the table
COMMENT ON TABLE public.business_info IS 'Stores basic information about businesses';

-- Add comments to the columns
COMMENT ON COLUMN public.business_info.id IS 'Unique identifier for each business';
COMMENT ON COLUMN public.business_info.name IS 'Name of the business';
COMMENT ON COLUMN public.business_info.description IS 'Description of the business';