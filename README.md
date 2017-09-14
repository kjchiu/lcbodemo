# Server

Jersey + embedded Jetty. Redis data store, Retrofit as HTTP client for lcbo api. Server is stateless so scaling is simple. There's also very little user state that matters. Caveat, all brittleness of home brewed auth schemes apply. Really we should just be using oAuth or something but that seems against the spirit of the challenge.

# Client
An exuse to mess around with react-redux after having used react + flux. Supports simplistic text queries for lcbo products, logged in users have access to their search history (limited to last 10 queries). 

Lack of telemtry for use case makes it hard to decide on any reasonable caching strategy (i.e. how unique are queries going to be). At the very least, lcbo-api returning full product info after a product query instead of just an array of ids prcludes caching individual products. It's possible to just ETL the entire lcbo-api inventory repeatedly but that again needs an explicit feature set to govern the schema. The only beneficial optimisation currently is adding page results to a ring buffer so revisiting previously (within reason) queried pages doesn't require server access.

## Deployment
`lcbo-server-1.0-SNAPSHOT-jar-with-dependencies.jar` is the self hosting rest server. Can be run anyways as long as there is `config/server.properties` accessibly from `pwd`. Template server.properties can be found in /`config` of repo root.
`lcbo-client-1.0-SNAPSHOT.zip` contains the `index.html` + `bundle.js` required by the client.

Assumes there's a reverse proxy hosting client assets at / and rewriting /rest to :$server.port/ as well as a redis instance running with default port on the same host as the lcbo-server jar.
