class ManageSectionPage {

    navigateEditPage () {
        cy.visit('/page-builder/pages/1010436/edit')
    }

    openManageSectionsPopup() {
        cy.get('.manageSections').eq(0).click();
    }

    seeElementOnManageSection(content, type, description) {
        if (type === "title" || type === "heading" || type === "button") {
            cy.contains(content);
        } else if (type === "icon") {
            if (content === "Six dots") {
                cy.get('img[src="/icons/drag.svg"]');
            } else if (content === "Pencil") {
                cy.get('[data-testId="editIcon"]');
            } else if (content === "Trash can") {
                cy.get('[data-testId="deleteIcon"]');
            } else if (content === "Cross-eye") {
                cy.get('[data-testId="visibilityIcon"]');
            }
        }
    }

    viewTrashIcon() {
        cy.get('[data-testId="deleteIcon"]');
    }

    clickTrashIcon() {
        cy.get('[data-testId="deleteIcon"]').eq(0).click();
    }

    viewDeleteConfirmationDialogText(texts) {
        texts.forEach((text) => {
            cy.contains(text);
        });
    }

    clickYesDeleteBtn() {
        cy.get('.yesDelete').eq(0).click()
    }

    closeDeleteConfirmationDialog() {
        cy.wait(1000);
        cy.get('.warningModalHeader').should('not.exist');
    }

    showDeleteConfirmationText(text) {
        cy.contains(text);
    }

    checkSectionDeleted() {
        cy.get('.manageSectionsCloseBtn').eq(0).click();
    }

    verifyManageSectionsHeader(title) {
        cy.contains(title)
    }

    clickAddNewSection() {
        cy.get('.addNewSectionBtn').eq(0).click({ force: true });
    }

    enterSectionName() {
        cy.get('.sectionName').eq(0).type("test new section");
    }

    clickAddSectionBtn() {
        cy.get('.addSectionBtn').eq(0).click();
    }

    checkAddedSectionExist() {
        cy.contains("test new section");
    }

    viewPencilIcon() {
        cy.get('[data-testId="editIcon"]');
    }

    clickPencilIcon() {
        cy.get('[data-testId="editIcon"]').eq(0).click();
    }

    viewEditSectionModalWindow() {
        cy.contains("Edit section");
    }

    modifySectionName() {
        const newSecName = Math.random().toString(36).substring(2, 12);
        cy.get('.sectionName').eq(0).clear().type(`Modified text section name ${newSecName}`);
    }

    clickSaveBtn() {
        cy.get('.saveChangesBtn').eq(0).click({ force: true });
    }

    closeEditSectionModal() {
        cy.contains("Edit section").should('not.be.visible');
    }

    checkConfirmationMessageShowing(text) {
        cy.contains(text);
    }

}

export const manageSectionPage = new ManageSectionPage()