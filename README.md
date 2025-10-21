# Solution 1.0

### Issues with `shouldDeleteProductSuccessfully()` in `ProductControllerTest`

There were two issues with the test: the path to the `DELETE` endpoint and the returntype of the `deleteProductById()` method.

#### Original Endpoint & Method:

    @DeleteMapping("/delete")
    public Mono<Void> deleteProductById(@PathVariable @Positive Long id) {

        return productService.deleteProductById(id)
                .map(product -> ResponseEntity.status(204)).then();
    }

---

**Path to Endpoint**

The path was `/api/v1/product/delete`, but the method parameter contains a `@PathVariable` for `id`. The variable needs to be included in the path, otherwise Spring can not match the path variable to the URL, which caused the test to return **404 NOT_FOUND**.

**Solution:**

Add `/{id}` to the endpoint so the full path looks like:

`/api/v1/product/delete/{id}`

---

**Returntype of method `deleteProductById()`**

The original returntype was `Mono<Void>` which does not allow returning a proper HTTP status. Because of this, even though the product was deleted, Spring returned **200 OK** instead of the expected **204 NO_CONTENT**, causing the test assertion to fail.

**Solution**

Change the method to return `Mono<ResponseEntity<Void>>` and map the result to `ResponseEntity.noContent()` after the delete operation:


    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteProductById(@PathVariable @Positive Long id) {

        return productService.deleteProductById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

---

# Code along - Fork Repository

This repository contains the 0.5 version of the project.  
Your task is to fork this repository, complete the missing parts, and build it up to version 1.0 on your own fork.

## Instructions

1. Fork this repository  
   Click the “Fork” button in the top-right corner of this page.  
   This creates a copy of the repo under your own GitHub account.

2. Clone your fork  
   git clone YOUR_URL_HERE

3. Work on your solution  
   - Make all your changes in your own fork.  
   - You can work directly in main (no need to create extra branches).

4. Commit and push your changes  
   git add .  
   git commit -m "My solution for 1.0"  
   git push

5. Submit your work  
   When you’re done, send me the link to your forked repository.

## 💡 Notes

- A fork is your own personal copy of this repository.  
  You can make any changes you want without affecting the original project.  
  It’s like duplicating a file and editing your own version of it.
- You do not need to open a Pull Request to the original repo.  
- You fully own your fork — no restrictions.  
- A solution branch will be added to this repo later for reference.  
- If something breaks, you can always re-fork or re-clone the original repo.
