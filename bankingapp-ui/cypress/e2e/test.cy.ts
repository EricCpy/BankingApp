describe('template spec', () => {
  const serverUrl = Cypress.env('serverUrl');

  beforeEach(function () {
    //auto login for all tests
    cy.intercept(
      {
        method: 'POST',
        url: serverUrl + 'user/login'
      },
      {
        fixture: 'login.json'
      }
    ).as('login');
    cy.intercept(
      {
        method: 'GET',
        url: serverUrl + 'banking/my'
      },
      {
        body: []
      }
    )
    cy.intercept(
      {
        method: 'POST',
        url: serverUrl + 'banking/create'
      },
      {
        statusCode: 200
      }
    )

    cy.visit('/');
    cy.get('[data-cy="loginButton"]').click();
    cy.get('input[formControlName="email"]').type("admin@admin.com");
    cy.get('input[formControlName="password"]').type("password");
    cy.get('[data-cy="loginFormButton"]').click();
    cy.wait('@login');
  })

  it('Login', () => {
    cy.contains('Accounts').should('exist');
    cy.contains('Login').should('not.exist');
  })
  
  it('Create Banking Account', () => {
    cy.get('mat-card').should('have.length', 2);
    cy.get('mat-card button').should('have.length', 2);
    cy.intercept(
      {
        method: 'GET',
        url: serverUrl + 'banking/my'
      },
      {
        fixture: 'accounts.json'
      }
    ).as('getBankingAccount');
    cy.get('[data-cy="createCheckingAccountButton"]').click();
    cy.wait('@getBankingAccount');
    cy.get('mat-card').should('have.length', 2);
    cy.get('mat-card button').should('have.length', 1);
    cy.contains('DE00000000000000000001').should('exist');
  })

  it('Get Account Transactions', () => {
    cy.intercept(
      {
        method: 'GET',
        url: serverUrl + 'banking/my'
      },
      {
        fixture: 'accounts.json'
      }
    ).as('getBankingAccount');
    cy.intercept(
      {
        method: 'GET',
        url: serverUrl + 'banking/transaction/my?iban=*'
      },
      {
        fixture: 'transactions.json'
      }
    ).as('getTransactions');
    cy.get('mat-card').eq(0).click();
    cy.wait('@getTransactions');
    cy.get('mat-card').should('have.length', 5);
  })
})