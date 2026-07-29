# Implementation Plan - Fix Add Button Visibility in DashboardActivity

The "Add" button fails to reappear after clicking "Cancel" because `selectedListState` is updated *after* the UI refresh method (`btnDeleteAdd`) is called. Additionally, the internal state management within `buttonCancel()` and `btnDeleteAdd()` is inconsistent.

## Proposed Changes

### [DashboardActivity](file:///D:/android-studio/VocabularyENGApplication/app/src/main/java/com/example/vocabularyengapplication/DashboardActivity.kt)

#### [MODIFY] [DashboardActivity.kt](file:///D:/android-studio/VocabularyENGApplication/app/src/main/java/com/example/vocabularyengapplication/DashboardActivity.kt)

- **Fix `btnCancel` listener**: Update `selectedListState = ListWordState.NORMAL` *before* calling `btnDeleteAdd()`.
- **Fix `ivDelete` listener**: Update `selectedListState = ListWordState.REMOVE` *before* calling `buttonCancel()`.
- **Refactor `buttonCancel()` and `btnDeleteAdd()`**:
    - Remove `selectedListState = ListWordState.NORMAL` from `buttonCancel()` as it contradicts the "Delete mode" (where Cancel is visible).
    - Ensure these methods only handle UI visibility, while `setProgressAndRefresh()` handles the complex visibility logic based on the already-updated state.
- **Fix `refreshAndRemove()`**: Ensure the state remains `REMOVE` if items still exist and we are in deletion mode.

## Verification Plan

### Manual Verification
1. Open the app and go to the Dashboard.
2. Ensure at least one vocabulary item exists.
3. Click the **Delete** icon.
    - Verify "Add" and "Delete" icons disappear.
    - Verify "Cancel" button appears.
    - Verify vocab items show delete indicators (handled by adapter).
4. Click the **Cancel** button.
    - Verify "Cancel" button disappears.
    - Verify "Add" and "Delete" icons reappear.
5. Add vocabulary until progress reaches 100%.
    - Verify "Add" button disappears.
6. Delete an item while in delete mode.
    - Verify the list refreshes and stays in delete mode (Cancel button still visible) if items remain.
