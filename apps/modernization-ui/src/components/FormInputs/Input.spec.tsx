import { Input } from './Input';
import { render } from '@testing-library/react';

describe('Input component tests', () => {
    describe('when there is an error', () => {
        it('should render an input and display the error', () => {
            const onChange = () => {};
            const { getByLabelText, getByTestId } = render(
                <Input
                    id="test-input-id"
                    name="test-input-name"
                    label="Test Input Label"
                    className="test-input-class-name"
                    htmlFor="test-input-id"
                    type="text"
                    onChange={onChange}
                    defaultValue="test-input-defaultValue"
                    error="invalid input"
                />
            );
            expect(getByLabelText('Test Input Label')).toBeTruthy();
            expect(getByTestId('errorMessage').innerHTML).toBe('invalid input');
        });
    });

    describe('when there is no error', () => {
        it('should render an input and display no errors', () => {
            const onChange = () => {};
            const { getByLabelText, queryByTestId } = render(
                <Input
                    id="test-input-id"
                    name="test-input-name"
                    label="Test Input Label"
                    className="test-input-class-name"
                    htmlFor="test-input-id"
                    type="text"
                    onChange={onChange}
                    defaultValue="test-input-defaultValue"
                />
            );
            expect(getByLabelText('Test Input Label')).toBeTruthy();
            expect(queryByTestId('errorMessage')).toBe(null);
        });
    });
});
