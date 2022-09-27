import logging
from locust import HttpUser, task, events

class getReviews(HttpUser):
    @task
    def cat(self):
        self.client.get("/api/review/329199")
        self.client.get("/api/review/444435")
        self.client.get("/api/review/444434")
        self.client.get("/api/review/165954")
        self.client.get("/api/review/165614")

@events.quitting.add_listener
def _(environment, **kw):
    if environment.stats.total.fail_ratio > 0.01:
        logging.error("Test failed due to failure ratio > 1%")
        environment.process_exit_code = 1
    elif environment.stats.total.avg_response_time > 30:
        logging.error("Test failed due to average response time ratio > 30 ms")
        environment.process_exit_code = 1
    elif environment.stats.total.get_response_time_percentile(0.95) > 800:
        logging.error("Test failed due to 95th percentile response time > 800 ms")
        environment.process_exit_code = 1
    else:
        environment.process_exit_code = 0
