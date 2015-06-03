Feature: Cancel Order
  In order to cancel the reservation

  A customer

  Should cancel the order

  Scenario: Cancel an unpaid order
    Given I placed an order
    When I cancel the order
    Then it should accept the cancellation
    And it should suggest no more further steps