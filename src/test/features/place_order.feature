Feature:  Place Order
  In order to buy foods from RestFriedChicken
  A customer
  Should place an order

  Scenario:
    When I place an order with items:
      | name       | quantity |
      | Fried Chicken |     1 |
      | Fried Octopus |     2 |
    Then it should accept the order
    And it should suggest making a payment
    And it should suggest cancelling the order
