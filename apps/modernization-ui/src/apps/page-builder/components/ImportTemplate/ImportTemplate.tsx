import React from 'react';
import { Button, Icon } from '@trussworks/react-uswds';
import './ImportTemplate.scss';

export const ImportTemplate = () => {
    return (
        <div className="import-template">
            <Button className="usa-button--unstyled close-btn" type={'button'} onClick={() => {}}>
                <Icon.Close />
            </Button>
            <h3 className="main-header-title">
                <Button className="usa-button--unstyled back-btn" type={'button'} onClick={() => {}}>
                    <Icon.ArrowBack />
                </Button>
                <span data-testid="header-title">Import template</span>
            </h3>
            <div className="drop-container">
                <div className="heading">
                    <label>Import a new template</label>
                </div>
                <label onChange={() => {}} htmlFor="importTempId">
                    <input name="" type="file" id="importTempId" hidden />
                    <div className="drop-area">
                        <Icon.Logout size={4} />
                        <label htmlFor="importTempId">
                            Drag & drop or <span>Choose file</span> to upload
                        </label>
                    </div>
                </label>
            </div>
            <div>
                <Button className="submit-btn" type="button" disabled>
                    Import
                </Button>
                <Button className="cancel-btn" type="button">
                    Cancel
                </Button>
            </div>
        </div>
    );
};
