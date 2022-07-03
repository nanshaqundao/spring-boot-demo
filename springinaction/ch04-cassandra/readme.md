# Cassandra sample

## Issue remaining
 - missing the part to load data (in dev) every time application starts like h2(data.sql) or hibernate(import.sql)
 - not embedded cassandra (the one used is docker instances)
 - thus the schema is not using recreate but create-if-not-exists