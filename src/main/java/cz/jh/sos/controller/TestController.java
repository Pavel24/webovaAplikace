package cz.jh.sos.controller;

import cz.jh.sos.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    JdbcTemplate jdbcTemplate;

    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/test")
    public String test() {
        return "Hello world!";
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable Long id) {

        List<Map<String, Object>> rows
                = jdbcTemplate.queryForList(
                "SELECT id, name, city FROM customer WHERE id = ?", id);

        Map<String, Object> map = rows.get(0);

        Customer customer = new Customer();
        customer.setId(((BigDecimal) map.get("id")).longValue());
        customer.setName((String) map.get("name"));
        customer.setCity((String) map.get("city"));
    }
        @GetMapping("/customer")
        public List<Customer> getCustomers(
                @RequestParam(required = false, defaultValue = "1") Integer pageNo) {
            return jdbcTemplate.query(
                    "SELECT id, name, city, grade FROM customer ORDER BY id LIMIT ?,?",
                    new Object[]{getHowMuchRowsToSkip(pageNo), PAGE_SIZE},
                    new BeanPropertyRowMapper<>(Customer.class));
        }

        private int getHowMuchRowsToSkip(int pageNo) {
            if (pageNo <= 0) {
                throw new IllegalArgumentException("invalid number of page");
            }

            return (pageNo - 1) * PAGE_SIZE;
        }


}
